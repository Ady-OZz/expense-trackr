package com.fullStack.expenseTracker.dataSeeders;

import com.fullStack.expenseTracker.enums.ERole;
import com.fullStack.expenseTracker.enums.ETransactionType;
import com.fullStack.expenseTracker.models.Category;
import com.fullStack.expenseTracker.models.Role;
import com.fullStack.expenseTracker.models.TransactionType;
import com.fullStack.expenseTracker.models.User;
import com.fullStack.expenseTracker.repository.CategoryRepository;
import com.fullStack.expenseTracker.repository.RoleRepository;
import com.fullStack.expenseTracker.repository.TransactionTypeRepository;
import com.fullStack.expenseTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Seeds default categories (expense + income) and an admin account on first start,
 * replacing the manual SQL inserts from the original README. Safe to run repeatedly.
 */
@Component
@Slf4j
public class DefaultDataSeeder implements ApplicationRunner {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TransactionTypeRepository transactionTypeRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${app.seed.categories:true}")
    private boolean seedCategories;

    @Value("${app.seed.admin.enabled:true}")
    private boolean seedAdmin;
    @Value("${app.seed.admin.username:admin}")
    private String adminUsername;
    @Value("${app.seed.admin.email:admin@example.com}")
    private String adminEmail;
    @Value("${app.seed.admin.password:Admin@123}")
    private String adminPassword;

    private static final List<String> EXPENSE_CATEGORIES = List.of(
            "Food & Dining", "Groceries", "Rent", "Transport & Fuel", "Electricity & Bills",
            "Mobile & Internet", "Shopping", "Health & Medical", "Education", "Entertainment",
            "Travel", "EMI & Loans", "Other");
    private static final List<String> INCOME_CATEGORIES = List.of(
            "Salary", "Freelance", "Business", "Investments", "Gift", "Other");

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (seedCategories) {
                seed(ETransactionType.TYPE_EXPENSE, EXPENSE_CATEGORIES);
                seed(ETransactionType.TYPE_INCOME, INCOME_CATEGORIES);
            }
            if (seedAdmin) {
                seedAdminUser();
            }
        } catch (Exception e) {
            log.error("Default data seeding failed: {}", e.getMessage());
        }
    }

    private void seed(ETransactionType typeName, List<String> names) {
        TransactionType type = transactionTypeRepository.findByTransactionTypeName(typeName);
        if (type == null) return;
        for (String name : names) {
            if (!categoryRepository.existsByCategoryNameAndTransactionType(name, type)) {
                categoryRepository.save(new Category(name, type, true));
            }
        }
    }

    private void seedAdminUser() {
        if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(adminUsername)) return;
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElse(null);
        if (adminRole == null) return;
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        User admin = new User(adminUsername, adminEmail, passwordEncoder.encode(adminPassword), null, null, true, roles);
        userRepository.save(admin);
        log.info("Seeded admin user '{}' ({})", adminUsername, adminEmail);
    }
}

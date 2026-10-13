// All money in this app is shown in Indian Rupees (INR, ₹) using Indian digit grouping (e.g. ₹1,25,000.50)
export const CURRENCY_SYMBOL = "₹";

export function formatINR(value) {
    const num = Number(value ?? 0);
    const safe = Number.isFinite(num) ? num : 0;
    return CURRENCY_SYMBOL + safe.toLocaleString("en-IN", { minimumFractionDigits: 0, maximumFractionDigits: 2 });
}

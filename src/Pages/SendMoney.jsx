import { useState, useEffect } from "react";
import api from "./Api/axiosClient";

export default function SendMoney() {
  const [accounts, setAccounts] = useState([]);
  const [fromAccount, setFromAccount] = useState("");
  const [toAccount, setToAccount] = useState("");
  const [amount, setAmount] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    loadAccounts();
  }, []);

  const loadAccounts = async () => {
    try {
      const res = await api.get("/api/accounts");
      setAccounts(res.data);
    } catch (e) {
      setMessage("Failed to load accounts");
    }
  };

  const sendMoney = async () => {
    setMessage("");

    try {
      const res = await api.post("/api/transfer", {
        fromAccountId: Number(fromAccount),
        toAccountId: Number(toAccount),
        amount: Number(amount)
      });

      setMessage(res.data.message);
    } catch (err) {
      setMessage(err.response?.data?.message || "Transfer failed");
    }
  };

  return (
    <div style={{ padding: "40px" }}>
      <h2>Send Money</h2>

      <div>
        <label>From Account:</label>
        <select value={fromAccount} onChange={(e) => setFromAccount(e.target.value)}>
          <option value="">Select…</option>
          {accounts.map(acc => (
            <option key={acc.accountId} value={acc.accountId}>
              {acc.accountId} — Balance: {acc.balance}
            </option>
          ))}
        </select>
      </div>

      <div>
        <label>To Account:</label>
        <select value={toAccount} onChange={(e) => setToAccount(e.target.value)}>
          <option value="">Select…</option>
          {accounts.map(acc => (
            <option key={acc.accountId} value={acc.accountId}>
              {acc.accountId}
            </option>
          ))}
        </select>
      </div>

      <div>
        <label>Amount:</label>
        <input
          type="number"
          min="1"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
      </div>

      <button onClick={sendMoney}>Send</button>

      <p style={{ marginTop: "20px" }}>{message}</p>
    </div>
  );
}

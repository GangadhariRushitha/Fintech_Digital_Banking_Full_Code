import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../Api/axiosClient";

export default function Transfer() {
  const navigate = useNavigate();
  const location = useLocation();

  // Get the default 'fromAccountId' from dashboard query param
  const queryParams = new URLSearchParams(location.search);
  const defaultFromId = queryParams.get("from");

  const [fromAccountId, setFromAccountId] = useState(defaultFromId || "");
  const [toAccountId, setToAccountId] = useState("");
  const [amount, setAmount] = useState("");
  const [currency, setCurrency] = useState("USD");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const handleTransfer = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    if (!fromAccountId || !toAccountId || !amount) {
      setMessage("Please fill all required fields.");
      setLoading(false);
      return;
    }

    try {
      const res = await api.post("/api/transfer", {
        fromAccountId: Number(fromAccountId),
        toAccountId: Number(toAccountId),
        amount: Number(amount),
        currency,
        description,
      });

      setMessage(`Success! Transaction ID: ${res.data.transactionId}`);
    } catch (err) {
      console.error("Transfer error:", err);
      setMessage("Transfer failed. Check console for details.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "20px", maxWidth: "500px", margin: "auto" }}>
      <h2>Transfer Money</h2>

      {message && <p>{message}</p>}

      <form onSubmit={handleTransfer}>
        <div style={{ marginBottom: "10px" }}>
          <label>From Account ID:</label>
          <input
            type="number"
            value={fromAccountId}
            onChange={(e) => setFromAccountId(e.target.value)}
            required
            style={{ width: "100%" }}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>To Account ID:</label>
          <input
            type="number"
            value={toAccountId}
            onChange={(e) => setToAccountId(e.target.value)}
            required
            style={{ width: "100%" }}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>Amount:</label>
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            required
            style={{ width: "100%" }}
          />
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>Currency:</label>
          <select
            value={currency}
            onChange={(e) => setCurrency(e.target.value)}
            style={{ width: "100%" }}
          >
            <option value="USD">USD</option>
            {/* Add other currencies if needed */}
          </select>
        </div>

        <div style={{ marginBottom: "10px" }}>
          <label>Description:</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            style={{ width: "100%" }}
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Processing..." : "Transfer"}
        </button>
      </form>

      <button
        style={{ marginTop: "15px" }}
        onClick={() => navigate("/dashboard")}
      >
        Back to Dashboard
      </button>
    </div>
  );
}

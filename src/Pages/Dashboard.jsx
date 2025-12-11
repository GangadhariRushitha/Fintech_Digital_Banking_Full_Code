import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../Api/axiosClient";

export default function Dashboard() {
  const [accounts, setAccounts] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        const res = await api.get("/api/accounts");
        console.log("Fetched accounts:", res.data);
        setAccounts(res.data);
      } catch (err) {
        console.error("Error fetching accounts:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchAccounts();
  }, []);

  if (loading) return <h2>Loading accounts...</h2>;

  return (
    <div style={{ padding: "20px" }}>
      <h2>Your Accounts</h2>

      {accounts.length === 0 ? (
        <p>No accounts found</p>
      ) : (
        accounts.map((acc) => (
          <div
            key={acc.accountId}
            style={{
              border: "1px solid #ccc",
              padding: "12px",
              borderRadius: "8px",
              marginBottom: "10px",
            }}
          >
            <h3>Account #{acc.accountId}</h3>
            <p>Owner User ID: {acc.ownerUserId}</p>
            <p>
              Balance: {acc.balance} {acc.currency}
            </p>

            <button
              onClick={() =>
                navigate(`/transfer?from=${acc.accountId}`)
              }
              style={{ padding: "8px 12px", marginTop: "10px" }}
            >
              Transfer Money
            </button>
          </div>
        ))
      )}
    </div>
  );
}

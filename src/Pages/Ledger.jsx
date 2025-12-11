// import { useState } from "react";
// import api from "../Api/axiosClient";

// export default function Ledger() {
//   const [accountId, setAccountId] = useState("");
//   const [entries, setEntries] = useState([]);

//   const loadLedger = async () => {
//     if (!accountId) return;
//     try {
//       const res = await api.get(`/ledger/account/${accountId}`);
//       setEntries(res.data);
//     } catch (err) {
//       console.error(err);
//       alert("Failed to load ledger. Make sure account exists.");
//     }
//   };

//   return (
//     <div className="min-h-screen flex justify-center bg-gray-100 p-4">
//       <div className="bg-white p-6 rounded shadow w-full max-w-md">
//         <h2 className="text-xl font-bold mb-4 text-center">Ledger</h2>
//         <input
//           type="number"
//           placeholder="Account ID"
//           className="w-full mb-3 p-2 border rounded"
//           value={accountId}
//           onChange={e => setAccountId(e.target.value)}
//         />
//         <button
//           className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
//           onClick={loadLedger}
//         >
//           Load Ledger
//         </button>

//         <ul className="mt-4 divide-y">
//           {entries.map(e => (
//             <li key={e.ledgerId} className="py-2 flex justify-between">
//               <span>{e.entryType} → {e.amount}</span>
//               <span>{new Date(e.entryTimestamp).toLocaleString()}</span>
//             </li>
//           ))}
//         </ul>
//       </div>
//     </div>
//   );
// }

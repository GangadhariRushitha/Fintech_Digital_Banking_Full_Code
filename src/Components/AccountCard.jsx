// // src/Components/AccountCard.jsx
// export default function AccountCard({ account, onViewLedger }) {
//   return (
//     <div className="border p-4 rounded shadow mb-3 flex justify-between items-center">
//       <div>
//         <p>
//           <strong>Account ID:</strong> {account.accountId}
//         </p>
//         <p>
//           <strong>Owner User ID:</strong> {account.ownerUserId}
//         </p>
//         <p>
//           <strong>Balance:</strong> {account.balance} {account.currency}
//         </p>
//       </div>
//       <button
//         className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
//         onClick={() => onViewLedger(account.accountId)}
//       >
//         View Ledger
//       </button>
//     </div>
//   );
// }

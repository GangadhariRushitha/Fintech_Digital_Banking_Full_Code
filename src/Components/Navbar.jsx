// import { Link, useNavigate } from "react-router-dom";

// export default function Navbar() {
//   const navigate = useNavigate();

//   const handleLogout = () => {
//     localStorage.removeItem("token"); // remove JWT
//     navigate("/login"); // redirect to login page
//   };

//   const token = localStorage.getItem("token");

//   return (
//     <nav className="bg-blue-500 text-white p-4 flex justify-between items-center">
//       <div className="font-bold text-lg">VaultCore Financial</div>
//       <div className="space-x-4">
//         {token ? (
//           <>
//             <Link to="/dashboard" className="hover:underline">
//               Dashboard
//             </Link>
//             <Link to="/transfer" className="hover:underline">
//               Transfer
//             </Link>
//             <Link to="/ledger" className="hover:underline">
//               Ledger
//             </Link>
//             <button onClick={handleLogout} className="bg-red-500 px-3 py-1 rounded hover:bg-red-600">
//               Logout
//             </button>
//           </>
//         ) : (
//           <Link to="/login" className="hover:underline">
//             Login
//           </Link>
//         )}
//       </div>
//     </nav>
//   );
// }

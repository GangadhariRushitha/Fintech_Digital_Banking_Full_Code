import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./Pages/Login";
import Register from "./Pages/register";
import Dashboard from "./Pages/Dashboard";
import Transfer from "./Pages/Transfer";
// import Ledger from "./Pages/Ledger";
// import SendMoney from "./Pages/SendMoney";

function App() {
  const token = localStorage.getItem("token");

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={token ? <Dashboard /> : <Navigate to="/login" />} />
        {/* <Route path="/transfer" element={token ? <Transfer /> : <Navigate to="/login" />} /> */}
        <Route path="/transfer" element={token ? <Transfer /> : <Navigate to="/login" />} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;

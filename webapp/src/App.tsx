import { Route, Routes } from "react-router";
import Layout from "./components/layout/layout";
import Settlements from "./view/settlements";
import SettlementDetails from "./view/settlement-detail";

function App() {
  return (
    <>
      <Layout>
        <Routes>
          <Route path="/" element={<Settlements />} />
          <Route path="/:settlementId" element={<SettlementDetails />} />
        </Routes>
      </Layout>
    </>
  );
}

export default App;

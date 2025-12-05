import { Routes, Route, BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

import AppLayout from "./shared/layouts/AppLayout";
import SignLayout from "./shared/layouts/SignLayout";
import SignUp from "./pages/SignUp";
import SignIn from "./pages/SignIn";
import Home from "./pages/Home/Page";
import Categories from "./pages/Categories";
import Settings from "./pages/Settings";
import Transactions from "./pages/transactions/Page";

function App() {
	const queryClient = new QueryClient();

	return (
		<>
			<QueryClientProvider client={queryClient}>
				<BrowserRouter>
					<Routes>
						<Route element={<SignLayout />}>
							<Route path="/signup" element={<SignUp />} />
							<Route path="/signin" element={<SignIn />} />
						</Route>

						<Route path="/" element={<AppLayout />}>
							<Route index element={<Home />} />
							<Route path="/settings" element={<Settings />} />
							<Route path="/categories" element={<Categories />} />
							<Route path="/transactions" element={<Transactions />} />
						</Route>
					</Routes>
				</BrowserRouter>
			</QueryClientProvider>
		</>
	);
}

export default App;

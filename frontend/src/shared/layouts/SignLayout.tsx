import { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";

const SignLayout = () => {
	const navigate = useNavigate();

	useEffect(() => {
		if (localStorage.getItem("token")) navigate("/");
	}, [navigate]);

	return (
		<div className="min-h-screen bg-black flex items-center justify-center px-4">
			<main className="w-full max-w-md bg-neutral-900/80 backdrop-blur-sm border border-neutral-800 rounded-2xl shadow-lg p-8">
				<h1 className="text-3xl font-bold text-center mb-2">ExpenseTracker</h1>
				<Outlet />
			</main>
		</div>
	);
};

export default SignLayout;

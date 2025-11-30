import { Outlet, useNavigate } from "react-router-dom";
import Sidebar from "../components/SideBar";
import { Suspense, useEffect } from "react";
import Loader from "../components/Loader";
import { useQuery } from "@tanstack/react-query";
import http from "../api/services/http";
import useProfileFormWidget from "../hooks/useProfileFormWidget";

const AppLayout = () => {
	const navigate = useNavigate();


	const { modal, setModalIsOpen } = useProfileFormWidget();

	const { data } = useQuery({
		queryKey: ["profileCheck"],
		queryFn: async () => {
			const response = await http.get("/profile/check");
			return response.data;
		},
	});

	useEffect(() => {
		if (!localStorage.getItem("token")) navigate("/signin");
	}, [navigate]);

	useEffect(() => {
		if (data && !data?.isFilled) setModalIsOpen(true);
	}, [data, setModalIsOpen]);

	return (
		<>
			<div className="h-screen w-screen bg-black flex items-center">
				<Sidebar />
				<main className="flex flex-col w-full h-11/12 overflow-y-auto mx-3">
					<Suspense fallback={<Loader />}>
						<Outlet />
					</Suspense>
				</main>
			</div>

			{modal}
		</>
	);
};

export default AppLayout;

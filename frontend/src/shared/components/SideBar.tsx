import { Link, useLocation, useNavigate } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
	faArrowLeft,
	faArrowRight,
	faArrowRightFromBracket,
	faBookmark,
	faCalendar,
	faCashRegister,
	faGear,
	faHouse,
	// faUser,
	type IconDefinition,
} from "@fortawesome/free-solid-svg-icons";
import { useIsMobile } from "../hooks/useIsMobile";
import { useState } from "react";

type TRoutes = { name: string; path: string; position: string; icon: IconDefinition };

const routes: TRoutes[] = [
	{ name: "Home", path: "/", position: "top", icon: faHouse },
	{ name: "Transactions", path: "/transactions", position: "top", icon: faCashRegister },
	{ name: "Categories", path: "/categories", position: "top", icon: faBookmark },
	{ name: "Calendar", path: "/calendar", position: "top", icon: faCalendar },
	{ name: "Settings", path: "/settings", position: "bottom", icon: faGear },
];

const NavElement = ({ route, isMobile }: { route: TRoutes; isMobile: boolean }) => {
	const location = useLocation();

	return (
		<Link
			to={route.path}
			className={`text-left px-6 py-3 mb-2 rounded-full font-medium transition-all duration-100 ${
				location.pathname === route.path ? "bg-gray-500" : "text-gray-300 hover:bg-gray-600"
			}`}>
			<div className={`flex ${isMobile && "justify-center"}`}>
				<span className="w-8">
					<FontAwesomeIcon size="lg" icon={route.icon} />
				</span>
				{!isMobile && route.name}
			</div>
		</Link>
	);
};

const Sidebar = () => {
	const { isMobile } = useIsMobile();
	const [showFullSidebar, setShowFullSidebar] = useState(!isMobile);
	const navigate = useNavigate();

	const sidearToggle = () => {
		setShowFullSidebar((prev) => !prev);
	};

	const logoutButton = () => {
		localStorage.removeItem("token");
		navigate("/signin");
	};

	return (
		<div
			className={`${
				showFullSidebar ? "w-64" : "w-14"
			} h-11/12 flex flex-col bg-neutral-900/80 backdrop-blur-sm border border-neutral-800 rounded-2xl shadow-lg ml-3 transition-all duration-100`}>
			{showFullSidebar && (
				<div className="text-2xl font-bold p-6 border-b border-gray-700 transition-all duration-100">
					ExpenseTracker
				</div>
			)}

			<nav className={`flex flex-col ${showFullSidebar ? "px-1.5 mt-6" : "px-0.5 mt-2"}`}>
				{routes.map(
					(route: TRoutes) =>
						route.position === "top" && (
							<NavElement
								key={route.path}
								route={route}
								isMobile={!showFullSidebar}
							/>
						),
				)}
			</nav>

			<nav
				className={`h-11/12 flex flex-col justify-end ${
					showFullSidebar ? "px-1.5" : "px-0.5"
				}`}>
				<div className="border-b border-gray-700 mb-6"></div>
				{routes.map(
					(route: TRoutes) =>
						route.position === "bottom" && (
							<NavElement
								key={route.path}
								route={route}
								isMobile={!showFullSidebar}
							/>
						),
				)}

				<button
					className={
						"text-left px-6 py-3 mb-2 rounded-full transition-colors duration-200 font-medium text-gray-300 hover:bg-gray-600"
					}
					onClick={logoutButton}>
					<div className={`flex ${!showFullSidebar && "justify-center"} text-red-900`}>
						<span className="w-8">
							<FontAwesomeIcon icon={faArrowRightFromBracket} size="lg"/>
						</span>
						{showFullSidebar && "Logout"}
					</div>
				</button>

				<button
					className={
						"text-left px-6 py-3 mb-2 rounded-full transition-colors duration-200 font-medium text-gray-300 hover:bg-gray-700"
					}
					onClick={sidearToggle}>
					<div className={`flex ${!showFullSidebar && "justify-center"}`}>
						<span className="w-8">
							{showFullSidebar ? (
								<FontAwesomeIcon icon={faArrowLeft} size="lg"/>
							) : (
								<FontAwesomeIcon icon={faArrowRight} size="lg"/>
							)}
						</span>
						{showFullSidebar && "Hide Sidebar"}
					</div>
				</button>
			</nav>
		</div>
	);
};

export default Sidebar;

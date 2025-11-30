import { useQuery } from "@tanstack/react-query";
import MenuBox from "../shared/components/MenuBox";
import ProfileForm from "../shared/widgets/ProfileForm";
import http from "../shared/api/services/http";
import Loader from "../shared/components/Loader";

const Settings = () => {
	const { data, isError, isPending } = useQuery({
		queryKey: ["profile"],
		queryFn: async () => {
			const response = await http.get("/profile");
			return response.data;
		},
	});

	return (
		<div className="space-y-3">
			<MenuBox>
				<h1 className="text-3xl font-semibold ">Settings</h1>
			</MenuBox>

			<MenuBox className="w-4/4 md:w-2/4 lg:w-1/4">
				<h1 className="text-2xl font-semibold ">Your Profile</h1>

				{isError ? (
					<p className="text-rose-400">Profile getting is failed. Please try again.</p>
				) : isPending ? (
					<Loader />
				) : (
					<ProfileForm data={data} onSubmit={() => {}} />
				)}
			</MenuBox>
		</div>
	);
};

export default Settings;

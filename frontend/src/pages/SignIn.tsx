import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Link, useNavigate } from "react-router-dom";
import http from "../shared/api/services/http";
import { useMutation } from "@tanstack/react-query";
import Loader from "../shared/components/Loader";
import type { AxiosError } from "axios";

const schema = z.object({
	username: z.string().min(1, "Enter your name"),
	password: z.string().min(7, "Password must be at least 7 characters"),
});

type FormSchema = z.infer<typeof schema>;

const SignIn = () => {
	const navigate = useNavigate();

	const {
		register,
		handleSubmit,
		formState: { errors, isSubmitting },
	} = useForm<FormSchema>({
		resolver: zodResolver(schema),
		mode: "onChange",
	});

	const { mutate, isSuccess, isError, isPending } = useMutation({
		mutationFn: async (data: FormSchema) => {
			const response = await http.post("/auth/login", data);
			return response.data;
		},
		onError: (error: AxiosError) => {
			console.error("Signup error:", error);
		},
		onSuccess: (data) => {
			console.log("Signup successful:", data);

			localStorage.setItem("token", data.accessToken);
			localStorage.setItem("refreshToken", data.refreshToken);
			localStorage.setItem("username", data.username);

			setTimeout(() => {
				navigate("/");
			}, 1000);
		},
	});

	const onSubmit = (data: FormSchema) => {
		console.log("Submitted data:", data);
		mutate(data);
	};

	return (
		<>
			<form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
				<h2 className="text-2xl font-semibold text-center mb-6">Sign in your account</h2>
				<div>
					<label className="block text-sm text-gray-300 mb-1">Username</label>
					<input
						{...register("username")}
						className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
						placeholder="Your name"
						aria-invalid={errors.username ? "true" : "false"}
					/>
					{errors.username && (
						<p className="mt-1 text-xs text-rose-400">{errors.username.message}</p>
					)}
				</div>

				<div>
					<label className="block text-sm text-gray-300 mb-1">Password</label>
					<input
						{...register("password")}
						type="password"
						className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
						placeholder="Password"
						aria-invalid={errors.password ? "true" : "false"}
					/>
					{errors.password && (
						<p className="mt-1 text-xs text-rose-400">{errors.password.message}</p>
					)}
				</div>

				<button
					type="submit"
					disabled={isSubmitting || isPending || isSuccess}
					className="w-full py-2 rounded-lg bg-linear-to-r from-indigo-600 to-indigo-400 font-medium hover:from-indigo-500 hover:to-indigo-300 transition">
					{isPending ? <Loader /> : "Sign In"}
				</button>

				{isError && (
					<p className="mt-2 text-center text-sm text-rose-400">
						Signip failed. Please try again.
					</p>
				)}

				{isSuccess && (
					<p className="mt-2 text-center text-sm text-green-400">Signin successful! </p>
				)}
			</form>

			<p className="mt-4 text-center text-sm text-gray-400">
				Not registered yet?{" "}
				<Link to="/signup" className="text-indigo-400">
					Register
				</Link>
			</p>
		</>
	);
};

export default SignIn;

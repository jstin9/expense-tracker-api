import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useForm } from "react-hook-form";
import z from "zod";
import Loader from "../components/Loader";
import { useEffect } from "react";
import http from "../api/services/http";

const currencyTypes = ["EUR", "USD", "UAH"] as const;

const schema = z.object({
	name: z.string().min(1, "Enter your name"),
	balance: z.number("Enter your balance with numbers").min(0, "Balance must be at least 0"),
	monthSalary: z
		.number("Enter your month salary with numbers")
		.min(0, "Month salary must be at least 0"),
	currencyType: z.enum(currencyTypes, "Select currency type"),
});

type FormSchema = z.infer<typeof schema>;

type Props = {
	data?: FormSchema;
	onSubmit: () => void;
};

const ProfileForm = ({ data, onSubmit }: Props) => {
	const {
		register,
		handleSubmit,
		formState: { errors },
		setValue,
		reset,
	} = useForm<FormSchema>({
		resolver: zodResolver(schema),
		mode: "onChange",
	});

	const mutateProfile = useMutation({
		mutationFn: async (data: FormSchema) => {
			const response = await http.put("/profile", data);
			return response.data;
		},
		onError: (error: AxiosError) => {
			console.log(error);
		},
		onSuccess: () => {
			console.log("Mutate successful:", data);

			setTimeout(() => {
				onSubmit();
			}, 1000);
		},
	});

	const onSubmitForm = (data: FormSchema) => {
		mutateProfile.mutate(data);
	};

	useEffect(() => {
		if (data) {
			reset(data);
		}
	}, [reset, data]);

	return (
		<form onSubmit={handleSubmit(onSubmitForm)} className="space-y-4">
			<div>
				<label className="block text-sm text-gray-300 mb-1">Name</label>
				<input
					{...register("name")}
					className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
					placeholder="Your name"
					aria-invalid={errors.name ? "true" : "false"}
					disabled={mutateProfile.isPending}
				/>
				{errors.name && <p className="mt-1 text-xs text-rose-400">{errors.name.message}</p>}
			</div>

			<div>
				<label className="block text-sm text-gray-300 mb-1">Balance</label>
				<input
					type="number"
					step="0.01"
					{...register("balance", {
						valueAsNumber: true,
						min: 0,
						onBlur: (e) => {
							const num = parseFloat(e.target.value);
							if (!isNaN(num)) setValue("balance", Number(num.toFixed(2)));
						},
					})}
					className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
					placeholder="Your balance"
					aria-invalid={errors.name ? "true" : "false"}
					disabled={mutateProfile.isPending}
				/>
				{errors.balance && (
					<p className="mt-1 text-xs text-rose-400">{errors.balance.message}</p>
				)}
			</div>

			<div>
				<label className="block text-sm text-gray-300 mb-1">Month Salary</label>
				<input
					type="number"
					step="0.01"
					{...register("monthSalary", {
						valueAsNumber: true,
						min: 0,
						onBlur: (e) => {
							const num = parseFloat(e.target.value);
							if (!isNaN(num)) setValue("monthSalary", Number(num.toFixed(2)));
						},
					})}
					className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
					placeholder="Your month salary"
					aria-invalid={errors.name ? "true" : "false"}
					disabled={mutateProfile.isPending}
				/>
				{errors.monthSalary && (
					<p className="mt-1 text-xs text-rose-400">{errors.monthSalary.message}</p>
				)}
			</div>

			<div>
				<label className="block text-sm text-gray-300 mb-1">Currency</label>
				<select
					{...register("currencyType")}
					className="w-full bg-gray-800 border border-neutral-700 rounded-md px-3 py-2 text-gray-400 focus:ring-indigo-500 focus:ring-2">
					<option className="bg-gray-800" value="">
						Choose currency
					</option>

					<hr />

					{currencyTypes.map((currency, key) => (
						<option key={key} className="bg-gray-800" value={currency}>
							{currency}
						</option>
					))}
				</select>
				{errors.currencyType && (
					<p className="mt-1 text-xs text-rose-400">{errors.currencyType.message}</p>
				)}
			</div>

			<div className="border-b border-gray-700 my-4"></div>

			{mutateProfile.isSuccess && (
				<p className="text-green-400">Profile saved successfully!</p>
			)}

			{mutateProfile.isError && (
				<p className="text-rose-400">Profile saved failed. Please try again.</p>
			)}

			<div className="flex justify-end gap-3">
				<button
					className="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500"
					disabled={mutateProfile.isPending || mutateProfile.isSuccess}
					type="submit">
					{mutateProfile.isPending ? <Loader /> : "Submit"}
				</button>
			</div>
		</form>
	);
};

export default ProfileForm;

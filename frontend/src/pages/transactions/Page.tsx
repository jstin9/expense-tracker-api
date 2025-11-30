import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import MenuBox from "../../shared/components/MenuBox";
import {
	faArrowDown,
	faArrowLeft,
	faArrowRight,
	faArrowUp,
	faEdit,
	faPlus,
	faTrash,
} from "@fortawesome/free-solid-svg-icons";
import Loader from "../../shared/components/Loader";
import { useMemo, useState } from "react";
import Modal from "../../shared/components/Modal";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, useWatch } from "react-hook-form";
import { useMutation, useQuery } from "@tanstack/react-query";
import http from "../../shared/api/services/http";
import type { AxiosError } from "axios";
import type { Transaction, TransactionPageResponse } from "../../entities/transactionTypes";
import FilterPanel from "./FilterPanel";
import useFilters from "./useFilters";

const Transactions = () => {
	const { queryParams, ...filters } = useFilters();
	const [currentPage, setCurrentPage] = useState<number>(0);

	const { data, isLoading, isError, refetch } = useQuery<TransactionPageResponse<Transaction>>({
		queryKey: ["transactions", currentPage, queryParams],
		queryFn: async () => {
			const response = await http.get("/transactions", {
				params: { page: currentPage, ...queryParams },
			});
			return response.data;
		},
	});

	const totalTableSize = useMemo(() => {
		if (!data) return 0;
		return data.size;
	}, [data]);

	const totalPages = useMemo(() => {
		if (!data) return 0;
		return data.totalPages;
	}, [data]);

	const filtredData = useMemo(() => {
		if (!data) return [];
		return data.content;
	}, [data]);

	const { form, handleAdd, handleEdit, handleDelete } = useModalForm({
		parentOnSubmit: () => {
			refetch();
		},
	});

	const pages = generatePages(currentPage + 1, totalPages);

	return (
		<div className="space-y-3">
			<MenuBox>
				<h1 className="text-3xl font-semibold ">Yours Transactions</h1>
			</MenuBox>
			<MenuBox>
				<button
					onClick={handleAdd}
					className="mb-4 px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500transition">
					<span className="w-8">
						<FontAwesomeIcon icon={faPlus} size="lg" />
					</span>
					Add Item
				</button>
				{form}

				<FilterPanel filters={filters} />

				<table className="w-full text-left text-gray-300">
					<thead className="bg-neutral-800/60 text-gray-200">
						<tr>
							<th className="px-4 py-3 w-16">#</th>
							<th className="px-4 py-3">Type</th>
							<th className="px-4 py-3">Category</th>
							<th className="px-4 py-3">Amount</th>
							<th className="px-4 py-3 w-100">Description</th>
							<th className="px-4 py-3">Date</th>

							<th className="px-4 py-3 w-40 text-center">Actions</th>
						</tr>
					</thead>

					<tbody>
						{isLoading && (
							<tr>
								<td colSpan={6} className="px-4 py-3">
									<Loader />
								</td>
							</tr>
						)}

						{isError && (
							<tr>
								<td colSpan={6} className="px-4 py-3">
									<p className="text-rose-400">Error loading transaction.</p>
								</td>
							</tr>
						)}

						{!isLoading &&
							!isError &&
							filtredData?.map((item: Transaction, index: number) => (
								<tr
									key={item.id}
									className="border-t border-neutral-700/40 hover:bg-neutral-800/30 transition">
									<td className="px-4 py-3">
										{currentPage * totalTableSize + index + 1}
									</td>
									<td className="px-4 py-3">
										<span className="w-8">
											{item.type == "INCOME" ? (
												<FontAwesomeIcon
													className="text-green-800"
													icon={faArrowUp}
												/>
											) : (
												<FontAwesomeIcon
													className="text-red-800"
													icon={faArrowDown}
												/>
											)}
										</span>
										{item.type}
									</td>
									<td className="px-4 py-3">{item.category.name}</td>
									<td className="px-4 py-3">{item.amount} </td>
									<td className="px-4 py-3">
										{item.description.length > 50
											? item.description.slice(0, 50) + "..."
											: item.description}
									</td>
									<td className="px-4 py-3">
										{new Date(item.date).toLocaleDateString("RU-ru")}{" "}
									</td>

									<td className="px-4 py-3 flex items-center justify-center gap-3">
										<button
											onClick={() => {
												handleEdit(item);
											}}
											className="px-3 py-1 text-sm rounded-lg bg-indigo-600 hover:bg-indigo-500 transition text-white flex">
											<span className="w-8">
												<FontAwesomeIcon icon={faEdit} size="lg" />
											</span>
											Edit
										</button>
										<button
											onClick={() => {
												handleDelete(item);
											}}
											className="px-3 py-1 text-sm rounded-lg bg-fuchsia-600/80 hover:bg-fuchsia-500 transition text-white flex">
											<span className="w-8">
												<FontAwesomeIcon icon={faTrash} size="lg" />
											</span>
											Delete
										</button>
									</td>
								</tr>
							))}
					</tbody>
				</table>

				{/* Pagination */}

				<div className="flex gap-0.5 mt-3">
					{/* Prev */}
					<button
						onClick={() => setCurrentPage((p) => Math.max(0, p - 1))}
						disabled={currentPage === 0}
						className="w-10 h-10 flex items-center justify-center border border-gray-600 rounded-l-lg hover:bg-gray-700 disabled:opacity-40 transition">
						<FontAwesomeIcon icon={faArrowLeft} />
					</button>

					{pages.map((page, index) =>
						page === "..." ? (
							<button
								key={index}
								disabled
								className="w-10 h-10 flex items-center justify-center border border-gray-600 text-gray-400">
								…
							</button>
						) : (
							<button
								key={index}
								onClick={() => setCurrentPage((page as number) - 1)}
								className={`w-10 h-10 flex items-center justify-center border border-gray-600 transition
          ${currentPage + 1 === page ? "bg-indigo-600 text-white" : "hover:bg-gray-700"}
        `}>
								{page}
							</button>
						),
					)}

					{/* Next */}
					<button
						onClick={() => setCurrentPage((p) => Math.min(totalPages - 1, p + 1))}
						disabled={currentPage === totalPages - 1}
						className="w-10 h-10 flex items-center justify-center border border-gray-600 rounded-r-lg hover:bg-gray-700 disabled:opacity-40 transition">
						<FontAwesomeIcon icon={faArrowRight} />
					</button>
				</div>
			</MenuBox>
		</div>
	);
};

function generatePages(currentPage: number, totalPages: number, delta = 2) {
	const pages = [];

	const left = Math.max(1, currentPage - delta);
	const right = Math.min(totalPages, currentPage + delta);

	if (left > 1) {
		pages.push(1);
	}

	if (left > 2) {
		pages.push("...");
	}

	for (let i = left; i <= right; i++) {
		pages.push(i);
	}

	if (right < totalPages - 1) {
		pages.push("...");
	}

	if (right < totalPages) {
		pages.push(totalPages);
	}

	return pages;
}

type FormProps = {
	parentOnSubmit: () => void;
};

const schema = z.object({
	id: z.number().optional(),
	crudType: z.enum(["POST", "PUT", "DELETE"]),
	amount: z.number("Enter your amount with numbers").min(0.01),
	type: z.enum(["INCOME", "EXPENSE"]),
	category_id: z.string().min(1, "Choose category"),
	description: z.string().max(255, { message: "Description must be at most 255 characters" }),
	date: z.string(),
});

type FormSchema = z.infer<typeof schema>;

const useModalForm = ({ parentOnSubmit }: FormProps) => {
	const [modalFormIsOpen, setModalFormIsOpen] = useState(false);
	const [modalFormTitle, setModalFormTitle] = useState("");
	const [isDelete, setIsDelete] = useState<boolean>(false);

	const categories = useQuery({
		queryKey: ["categories"],
		queryFn: async () => {
			const response = await http.get("/categories");
			return response.data;
		},
	});

	const {
		mutate,
		isSuccess,
		isError: isMutateError,
		isPending,
		reset: mutateReset,
	} = useMutation({
		mutationFn: async (data: FormSchema) => {
			const response =
				data.crudType == "POST"
					? await http.post("/transactions", data)
					: data.crudType == "PUT"
					? await http.put(`/transactions/${data.id}`, data)
					: await http.delete(`/transactions/${data.id}`);

			return response.data;
		},
		onError: (error: AxiosError) => {
			console.error("error:", error);
		},
		onSuccess: (data) => {
			console.log("Mutate successful:", data);

			setTimeout(() => {
				setModalFormIsOpen(false);
				mutateReset();
				formReset();
			}, 1000);

			parentOnSubmit();
		},
		retry: 0,
	});

	const {
		register,
		handleSubmit,
		setValue,
		control,
		formState: { errors },
		reset: formReset,
	} = useForm<FormSchema>({
		resolver: zodResolver(schema),
		mode: "onChange",
		defaultValues: { type: "EXPENSE", date: new Date().toISOString().split("T")[0] },
	});

	const selected = useWatch({ control, name: "type" }) as FormSchema["type"];

	const handleModalClose = () => {
		setModalFormIsOpen(false);
	};

	const handleAdd = () => {
		formReset();
		setIsDelete(false);
		setModalFormTitle("Add Transaction");
		setValue("id", 0);
		setValue("crudType", "POST");

		setModalFormIsOpen(true);
	};

	const handleEdit = (item: Transaction) => {
		formReset();
		setIsDelete(false);
		setModalFormTitle("Edit Transaction");

		setValue("id", item.id);
		setValue("crudType", "PUT");
		setValue("amount", item.amount);
		setValue("type", item.type);

		setValue("category_id", item.category.id.toString());
		setValue("description", item.description);
		setValue("date", item.date);

		setModalFormIsOpen(true);
	};

	const handleDelete = (item: Transaction) => {
		formReset();
		setIsDelete(true);
		setModalFormTitle(`Delete Transaction`);

		setValue("id", item.id);
		setValue("crudType", "DELETE");
		setValue("amount", item.amount);
		setValue("type", item.type);

		setValue("category_id", item.category.id.toString());
		setValue("description", item.description);
		setValue("date", item.date);
		setModalFormIsOpen(true);
	};

	const onSubmit = (data: FormSchema) => {
		console.log(data);
		mutate(data);
	};

	return {
		handleAdd,
		handleEdit,
		handleDelete,
		form: (
			<Modal isOpen={modalFormIsOpen} onClose={handleModalClose} title={modalFormTitle}>
				<form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
					<input {...register("id")} type="hidden" />
					<input {...register("crudType")} type="hidden" />
					<div>
						<label className="block text-sm text-gray-300 mb-1">Amount</label>
						<input
							type="number"
							step="0.01"
							{...register("amount", {
								valueAsNumber: true,
								min: 0,
								onBlur: (e) => {
									const num = parseFloat(e.target.value);
									if (!isNaN(num)) setValue("amount", Number(num.toFixed(2)));
								},
							})}
							className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
							placeholder="Enter amount"
							aria-invalid={errors.amount ? "true" : "false"}
							disabled={isPending || isDelete}
						/>
						{errors.amount && (
							<p className="mt-1 text-xs text-rose-400">{errors.amount.message}</p>
						)}
					</div>

					<div>
						<label className="block text-sm text-gray-300 mb-1">Type</label>

						{/* radiogroup wrapper */}
						<div
							role="radiogroup"
							aria-label="Transaction type"
							className="inline-flex rounded-md overflow-hidden border border-neutral-700">
							{/* INCOME */}
							<label
								className={`flex items-center cursor-pointer px-4 py-2 select-none transition ${
									selected === "INCOME"
										? "bg-indigo-600"
										: "bg-gray-800 text-gray-200"
								}`}>
								<input
									{...register("type")}
									disabled={isPending || isDelete}
									type="radio"
									value="INCOME"
									className="sr-only"
									aria-checked={selected === "INCOME"}
								/>
								<span className="text-sm font-medium">Income</span>
							</label>

							{/* EXPENSE */}
							<label
								className={`flex items-center cursor-pointer px-4 py-2 select-none transition ${
									selected === "EXPENSE"
										? "bg-indigo-600"
										: "bg-gray-800 text-gray-200"
								}`}>
								<input
									{...register("type")}
									disabled={isPending || isDelete}
									type="radio"
									value="EXPENSE"
									className="sr-only"
									aria-checked={selected === "EXPENSE"}
								/>
								<span className="text-sm font-medium">Expense</span>
							</label>
						</div>

						{errors.type && (
							<p className="mt-1 text-xs text-rose-400">{errors.type.message}</p>
						)}
					</div>

					<div>
						<label className="block text-sm text-gray-300 mb-1">Category</label>

						{categories.isError && (
							<p className="mt-1 text-xs text-rose-400">
								Categories loading failed. Please try again.
							</p>
						)}
						{categories.isPending && <Loader />}

						<select
							{...register("category_id")}
							disabled={isPending || isDelete || categories.isPending}
							className="w-full bg-gray-800 border border-neutral-700 rounded-md px-3 py-2 text-gray-400 focus:ring-indigo-500 focus:ring-2">
							<option className="bg-gray-800" value="">
								Choose category
							</option>

							{categories.data?.map((category: { id: number; name: string }) => (
								<option
									key={category.id}
									className="bg-gray-800"
									value={category.id}>
									{category.name}
								</option>
							))}
						</select>

						{errors.category_id && (
							<p className="mt-1 text-xs text-rose-400">
								{errors.category_id.message}
							</p>
						)}
					</div>

					<div>
						<label className="block text-sm text-gray-300 mb-1">Description</label>

						<textarea
							{...register("description")}
							className="w-full h-24 bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500 resize-none"
							placeholder="Enter description..."
							aria-invalid={errors.description ? "true" : "false"}
							disabled={isPending || isDelete}
						/>

						{errors.description && (
							<p className="mt-1 text-xs text-rose-400">
								{errors.description.message}
							</p>
						)}
					</div>

					<div>
						<label className="block text-sm text-gray-300 mb-1">Date</label>
						<input
							type="date"
							{...register("date")}
							className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
							placeholder="Enter date"
							aria-invalid={errors.date ? "true" : "false"}
							disabled={isPending || isDelete}
						/>
						{errors.date && (
							<p className="mt-1 text-xs text-rose-400">{errors.date.message}</p>
						)}
					</div>

					<div className="border-b border-gray-700 my-4"></div>

					{isSuccess && <p className="text-green-400">Transaction saved successfully!</p>}
					{isMutateError && (
						<p className="text-rose-400">Transaction saved failed. Please try again.</p>
					)}

					<div className="flex justify-end gap-3">
						<button
							className="px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500"
							disabled={isPending || isSuccess}
							type="submit">
							{isPending ? <Loader /> : "Submit"}
						</button>
						<button
							type="button"
							onClick={handleModalClose}
							className="px-4 py-2 rounded-lg bg-fuchsia-600 hover:bg-fuchsia-500"
							disabled={isPending}>
							Close
						</button>
					</div>
				</form>
			</Modal>
		),
	};
};

export default Transactions;

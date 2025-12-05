import { useMemo, useState } from "react";
import MenuBox from "../shared/components/MenuBox";
import Modal from "../shared/components/Modal";
import z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useMutation, useQuery } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import http from "../shared/api/services/http";
import Loader from "../shared/components/Loader";
import { faEdit, faPlus, faTrash } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const schema = z.object({
	id: z.number().optional(),
	type: z.enum(["POST", "PUT", "DELETE"]),
	name: z.string().min(1, "Enter category name"),
});
type FormSchema = z.infer<typeof schema>;

const Categories = () => {
	const [modalFormIsOpen, setModalFormIsOpen] = useState(false);
	const [modalFormTitle, setModalFormTitle] = useState<string>("");
	const [isDelete, setIsDelete] = useState<boolean>(false);

	const [searchData, setSearchData] = useState<string>("");

	const { data, isLoading, isError, refetch } = useQuery<FormSchema[]>({
		queryKey: ["categories"],
		queryFn: async () => {
			const response = await http.get("/categories");
			return response.data;
		},
	});

	const filteredData = useMemo(() => {
  if (!data) return [];
  return data.filter(item =>
    item.name.toLowerCase().includes(searchData.toLowerCase())
  );
}, [data, searchData]);

	const {
		register,
		handleSubmit,
		setValue,
		formState: { errors },
		reset: formReset,
	} = useForm<FormSchema>({
		resolver: zodResolver(schema),
		mode: "onChange",
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
				data.type == "POST"
					? await http.post("/categories", data)
					: data.type == "PUT"
					? await http.put(`/categories/${data.id}`, data)
					: await http.delete(`/categories/${data.id}`);

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

			refetch();
		},
		retry: 0,
	});

	const handleAdd = () => {
		formReset();
		setIsDelete(false);
		setModalFormTitle("Add Category");
		setValue("id", 0);
		setValue("type", "POST");
		setModalFormIsOpen(true);
	};

	const handleEdit = (item: FormSchema) => {
		formReset();
		setIsDelete(false);
		setModalFormTitle("Edit Category");
		setValue("id", item.id);
		setValue("type", "PUT");
		setValue("name", item.name);
		setModalFormIsOpen(true);
	};

	const handleDelete = (item: FormSchema) => {
		formReset();
		setIsDelete(true);
		setValue("id", item.id);
		setValue("type", "DELETE");
		setValue("name", item.name);
		setModalFormTitle(`Delete Category: ${item.name}`);
		setModalFormIsOpen(true);
	};

	const onSubmit = (data: FormSchema) => {
		console.log(data);
		mutate(data);
	};

	const handleModalClose = () => {
		if (!isPending) setModalFormIsOpen(false);
	};

	return (
		<>
			<MenuBox>
				<h1 className="text-3xl font-semibold ">Yours Categories</h1>
			</MenuBox>

			<MenuBox className="mt-3">
				<div>
					<div className="flex justify-between">
						<button
							onClick={handleAdd}
							className="mb-4 px-4 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500transition">
							<span className="w-8">
								<FontAwesomeIcon icon={faPlus} size="lg" />
							</span>
							Add Item
						</button>

						<div>
							<input
								type="search"
								className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
								placeholder="Search"
								value={searchData}
								onChange={(e) => setSearchData(e.target.value)}
							/>
						</div>
					</div>

					<Modal
						isOpen={modalFormIsOpen}
						onClose={handleModalClose}
						title={modalFormTitle}>
						<form onSubmit={handleSubmit(onSubmit)}>
							<div>
								<input {...register("id")} type="hidden" />
								<input {...register("type")} type="hidden" />
								<label className="block text-sm text-gray-300 mb-1">Category</label>
								<input
									{...register("name")}
									className="w-full bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-500"
									placeholder="Category name"
									aria-invalid={errors.name ? "true" : "false"}
									disabled={isPending || isDelete}
								/>
								{errors.name && (
									<p className="mt-1 text-xs text-rose-400">
										{errors.name.message}
									</p>
								)}
							</div>

							<div className="border-b border-gray-700 my-4"></div>

							{isSuccess && (
								<p className="text-green-400">Category saved successfully!</p>
							)}
							{isMutateError && (
								<p className="text-rose-400">
									Category saved failed. Please try again.
								</p>
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

					<table className="w-full text-left text-gray-300">
						<thead className="bg-neutral-800/60 text-gray-200">
							<tr>
								<th className="px-4 py-3 w-16">#</th>
								<th className="px-4 py-3">Name</th>
								<th className="px-4 py-3 w-40 text-center">Actions</th>
							</tr>
						</thead>

						<tbody>
							{isLoading && (
								<tr>
									<td colSpan={3} className="px-4 py-3">
										<Loader />
									</td>
								</tr>
							)}

							{isError && (
								<tr>
									<td colSpan={3} className="px-4 py-3">
										<p className="text-rose-400">Error loading categories.</p>
									</td>
								</tr>
							)}

							{!isLoading &&
								!isError &&
								filteredData?.map((item: FormSchema, index: number) => (
									<tr
										key={item.id}
										className="border-t border-neutral-700/40 hover:bg-neutral-800/30 transition">
										<td className="px-4 py-3">{index + 1}</td>
										<td className="px-4 py-3">{item.name}</td>
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
				</div>
			</MenuBox>
		</>
	);
};

export default Categories;

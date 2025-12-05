import http from "../../shared/api/services/http";
import Loader from "../../shared/components/Loader";
import { useQuery } from "@tanstack/react-query";

const types = ["INCOME", "EXPENSE"];
const sizeOptions = [50, 80, 100];

type Props = {
	filters: {
		type: string;
		setType: (v: string) => void;
		categoryId: string;
		setCategoryId: (v: string) => void;
		minAmount: string;
		setMinAmount: (v: string) => void;
		maxAmount: string;
		setMaxAmount: (v: string) => void;
		startDate: string;
		setStartDate: (v: string) => void;
		endDate: string;
		setEndDate: (v: string) => void;
		sort: string;
		setSort: (v: string) => void;
		size: string;
		setSize: (v: string) => void;
		resetFilters: () => void;
	};
};

const FilterPanel = ({
	filters: {
		type,
		setType,
		categoryId,
		setCategoryId,
		minAmount,
		setMinAmount,
		maxAmount,
		setMaxAmount,
		startDate,
		setStartDate,
		endDate,
		setEndDate,
		sort,
		setSort,
		size,
		setSize,
		resetFilters,
	},
}: Props) => {
	const categories = useQuery({
		queryKey: ["categories"],
		queryFn: async () => {
			const response = await http.get("/categories");
			return response.data;
		},
	});

	return (
		<div className="flex gap-3 items-end mb-3 flex-wrap">
			{/* Type */}
			<div>
				<label className="block text-sm text-gray-300 mb-1">Type</label>
				<select
					className="w-full bg-gray-800 border border-neutral-700 rounded-md px-3 py-2 text-gray-400 focus:ring-indigo-500 focus:ring-2"
					value={type}
					onChange={(e) => setType(e.target.value)}>
					<option className="bg-gray-800" value="">
						All types
					</option>
					{types.map((value) => (
						<option key={value} className="bg-gray-800" value={value}>
							{value}
						</option>
					))}
				</select>
			</div>

			{/* Category */}
			<div>
				{categories.isError && (
					<p className="mt-1 text-xs text-rose-400">
						Categories loading failed. Please try again.
					</p>
				)}

				{categories.isPending && <Loader />}

				<select
					className="w-full bg-gray-800 border border-neutral-700 rounded-md px-3 py-2 text-gray-400 focus:ring-indigo-500 focus:ring-2"
					value={categoryId}
					onChange={(e) => setCategoryId(e.target.value)}>
					<option className="bg-gray-800" value="">
						All categories
					</option>
					{categories.data?.map((value: { id: number; name: string }) => (
						<option key={value.id} className="bg-gray-800" value={value.id}>
							{value.name}
						</option>
					))}
				</select>
			</div>

			{/* Sort */}
			<div>
				<label className="block text-sm text-gray-300 mb-1">Sort</label>

				<div
					role="radiogroup"
					aria-label="sort"
					className="inline-flex rounded-md overflow-hidden border border-neutral-700">
					{/* ASC */}
					<label
						className={`flex items-center cursor-pointer px-4 py-2 select-none transition ${
							sort === "ASC" ? "bg-indigo-600" : "bg-gray-800 text-gray-200"
						}`}>
						<input
							type="radio"
							value="INCOME"
							className="sr-only"
							aria-checked={sort === "ASC"}
							checked={sort === "ASC"}
							onChange={() => setSort("ASC")}
						/>
						<span className="text-sm font-medium">ASC</span>
					</label>

					{/* DESC */}
					<label
						className={`flex items-center cursor-pointer px-4 py-2 select-none transition ${
							sort === "DESC" ? "bg-indigo-600" : "bg-gray-800 text-gray-200"
						}`}>
						<input
							type="radio"
							value="EXPENSE"
							className="sr-only"
							aria-checked={sort === "DESC"}
							checked={sort === "DESC"}
							onChange={() => setSort("DESC")}
						/>
						<span className="text-sm font-medium">DESC</span>
					</label>
				</div>
			</div>

			{/* Amount */}
			<div className="flex gap-2">
				<div>
					<label className="block text-sm text-gray-300 mb-1">Min amount</label>
					<input
						type="number"
						step="0.01"
						placeholder="Enter min amount"
						className="w-28 bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-indigo-500"
						value={minAmount}
						onChange={(e) => setMinAmount(e.target.value)}
					/>
				</div>
				<span className="text-3xl flex items-end p-1">-</span>
				<div>
					<label className="block text-sm text-gray-300 mb-1">Max amount</label>
					<input
						type="number"
						step="0.01"
						placeholder="Enter max amount"
						className="w-28 bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-indigo-500"
						value={maxAmount}
						onChange={(e) => setMaxAmount(e.target.value)}
					/>
				</div>
			</div>

			{/* Start / End Date */}
			<div className="flex gap-2">
				<div>
					<label className="block text-sm text-gray-300 mb-1">Start date</label>
					<input
						type="date"
						className="w-32 bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-indigo-500"
						value={startDate}
						onChange={(e) => setStartDate(e.target.value)}
					/>
				</div>
				<span className="text-3xl flex items-end p-1">-</span>
				<div>
					<label className="block text-sm text-gray-300 mb-1">End date</label>
					<input
						type="date"
						className="w-32 bg-gray-800 placeholder-gray-400 border border-neutral-700 rounded-md px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-indigo-500"
						value={endDate}
						onChange={(e) => setEndDate(e.target.value)}
					/>
				</div>
			</div>

			{/* Size */}
			<div>
				<label className="block text-sm text-gray-300 mb-1">Size</label>
				<select
					className="w-full bg-gray-800 border border-neutral-700 rounded-md px-3 py-2 text-gray-400 focus:ring-indigo-500 focus:ring-2"
					value={size}
					onChange={(e) => setSize(e.target.value)}>
					<option className="bg-gray-800" value="20">
						20
					</option>
					{sizeOptions.map((value) => (
						<option key={value} className="bg-gray-800" value={value}>
							{value}
						</option>
					))}
				</select>
			</div>

			{/* Reset */}
			<div>
				<button
					className="px-3 py-2 rounded-lg bg-indigo-600 hover:bg-indigo-500"
					onClick={resetFilters}>
					Clear
				</button>
			</div>
		</div>
	);
};

export default FilterPanel;

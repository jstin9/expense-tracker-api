import { useQuery } from "@tanstack/react-query";
import http from "../api/services/http";
import Loader from "../components/Loader";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowDown, faArrowUp } from "@fortawesome/free-solid-svg-icons";

const MonthlyIncomeExpense = () => {
	const today = new Date();
	const name = localStorage.getItem("username");
	const chartQuery = useQuery({
		queryKey: ["chartIncomeExpense"],
		queryFn: async () => {
			const response = await http.get("/statistics/income-expense", {
				params: {
					from: `${today.getFullYear()}-${today.getMonth() + 1}-01`,
					to: `${today.getFullYear()}-${today.getMonth() + 1}-${new Date(
						today.getFullYear(),
						today.getMonth() + 1,
						0,
					).getDate()}`,
				},
			});
			return response.data;
		},
	});

	return (
		<div className="w-2/2 h-full flex flex-col p-4">
			{chartQuery.isLoading && <Loader />}

			{chartQuery.isError && <p className="text-rose-400">Error loading chart.</p>}

			{!chartQuery.isError && !chartQuery.isLoading && (
				<>
					<h1 className="text-4xl">Hello, {name}!</h1>

					<div className="h-full flex mt-4">
						<div className="flex flex-col justify-center">
							<h2 className="text-3xl ">
								<span className="w-8">
									<FontAwesomeIcon className="text-green-800" icon={faArrowUp} />
								</span>
								{amountFormat(40000)}
							</h2>
							<h2 className="text-xl ml-2">Income</h2>
						</div>
						<h2 className="text-5xl flex flex-col items-center justify-center mx-5">
							|
						</h2>
						<div className="flex flex-col justify-center">
							<h2 className="text-3xl">
								<span className="w-8">
									<FontAwesomeIcon className="text-red-800" icon={faArrowDown} />
								</span>
								{amountFormat(400000)}
							</h2>
							<h2 className="text-xl ml-2">Expense</h2>
						</div>
					</div>
				</>
			)}
		</div>
	);
};

const amountFormat = (amount: number): string => {
  if (amount >= 1_000_000) {

    return +(amount / 1_000_000).toFixed(1) + "M";
  } else if (amount >= 1_000) {

    return +(amount / 1_000).toFixed(1) + "K";
  } else {
    return amount.toString();
  }
};

export default MonthlyIncomeExpense;

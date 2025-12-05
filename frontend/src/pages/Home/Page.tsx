import MenuBox from "../../shared/components/MenuBox";
import MonthlyChart from "../../shared/widgets/MonthlyChart";
import MonthlyIncomeExpense from "../../shared/widgets/MonthlyIncomeExpense";
import TinyBalanceChart from "../../shared/widgets/TinyBalanceChart";

const Home = () => {
	return (
		<div className="grid grid-cols-5 gap-3">
			<MenuBox className="col-span-5">
				<h1 className="text-3xl font-semibold">Home</h1>
			</MenuBox>

			<MenuBox className="col-span-3 row-span-1">
				<MonthlyIncomeExpense />
			</MenuBox>

			<MenuBox className="col-span-1 row-span-1">
				<TinyBalanceChart/>
			</MenuBox>
			<MenuBox className="col-span-1 row-span-2">
				test
			</MenuBox>

			<MenuBox className="h-80 col-span-2 row-span-2">
				<MonthlyChart />
			</MenuBox>
		</div>
	);
};

export default Home;

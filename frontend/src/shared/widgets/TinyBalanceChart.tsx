import { useQuery } from "@tanstack/react-query";
import http from "../api/services/http";
import Loader from "../components/Loader";
import { useMemo } from "react";
import {
	LineChart,
	Line,
	XAxis,
	YAxis,
	Tooltip,
	ResponsiveContainer,
	CartesianGrid,
} from "recharts";

const monthNames = [
	"Jan",
	"Feb",
	"Mar",
	"Apr",
	"May",
	"Jun",
	"Jul",
	"Aug",
	"Sep",
	"Oct",
	"Nov",
	"Dec",
];

type chartQueryData = {
	month: number;
	expense: number;
	income: number;
	balance?: number;
};

const TinyBalanceChart = () => {
	const chartQuery = useQuery({
		queryKey: ["chartMonthly"],
		queryFn: async () => {
			const response = await http.get("/statistics/monthly");
			return response.data;
		},
	});

	const filtredChartData = useMemo(() => {
		if (!chartQuery?.data) return [];
		return chartQuery.data.map((data: chartQueryData) => {
			return {
				...data,
				balance: data.income - data.expense,
			};
		});
	}, [chartQuery]);

	return (
		<div className="w-2/2 h-full flex flex-col justify-center items-center">
			{chartQuery.isLoading && <Loader />}

			{chartQuery.isError && <p className="text-rose-400">Error loading chart.</p>}

			{!chartQuery.isError && !chartQuery.isLoading && (
				<>
					<h1>Monthy Balance</h1>

					<ResponsiveContainer width="100%" height="100%">
						<LineChart
							data={filtredChartData}
							margin={{ top: 10, right: 10, bottom: 10, left: 0 }}>
							{/* GRID */}
							<CartesianGrid stroke="#1f2337" strokeDasharray="3 3" />

							{/* AXES */}
							<XAxis
								dataKey="month"
								stroke="#a5b4fc"
								axisLine={false}
								tickLine={false}
								// tickFormatter={month - 1}
							/>
							<YAxis
								stroke="#a5b4fc"
								width={38}
								axisLine={false}
								tickLine={false}
								tickFormatter={(v) => (v >= 1000 ? `${(v / 1000).toFixed(0)}k` : v)}
							/>

							{/* TOOLTIP */}
							<Tooltip
								cursor={{ stroke: "rgba(255,255,255,0.1)" }}
								contentStyle={{
									background: "#14182f",
									border: "1px solid #2d3252",
									borderRadius: "6px",
									color: "#e5e7eb",
								}}
								formatter={(value: number) => value.toFixed(2)}
								labelFormatter={(label) => `Month: ${monthNames[label]}`}
							/>

							{/* GRADIENT */}
							<defs>
								<linearGradient id="lineGradient" x1="0" y1="0" x2="0" y2="1">
									<stop offset="0%" stopColor="#4ade80" />
									<stop offset="100%" stopColor="#3b82f6" />
								</linearGradient>
							</defs>

							{/* LINE */}
							<Line
								type="monotone"
								dataKey="balance"
								stroke="url(#lineGradient)"
								strokeWidth={3}
								dot={{ r: 3, stroke: "#fff", strokeWidth: 1 }}
								activeDot={{ r: 5 }}
							/>
						</LineChart>
					</ResponsiveContainer>
				</>
			)}
		</div>
	);
};

export default TinyBalanceChart;

import { useQuery } from "@tanstack/react-query";
import http from "../api/services/http";
import Loader from "../components/Loader";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid } from "recharts";

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

const MonthlyChart = () => {
	const chartQuery = useQuery({
		queryKey: ["chartMonthly"],
		queryFn: async () => {
			const response = await http.get("/statistics/monthly");
			return response.data;
		},
	});

	return (
		<div className="w-2/2 h-full flex flex-col justify-center items-center">
			{chartQuery.isLoading && <Loader />}

			{chartQuery.isError && <p className="text-rose-400">Error loading chart.</p>}

			{!chartQuery.isError && !chartQuery.isLoading && (
				<>
					<h1>Monthly Income</h1>

					<ResponsiveContainer width="100%" height="100%">
						<BarChart data={chartQuery.data} barCategoryGap={6}>
							{/* GRID */}
							<CartesianGrid stroke="#1f2337" strokeDasharray="3 3" />

							{/* AXES */}
							<XAxis
								dataKey="month"
								stroke="#a5b4fc"
								tick={{ fill: "#a5b4fc", fontSize: 12 }}
								tickFormatter={(month) => monthNames[month - 1]}
							/>

							<YAxis
								stroke="#a5b4fc"
								tick={{ fill: "#a5b4fc", fontSize: 11 }}
								width={38}
								tickFormatter={(v) => (v >= 1000 ? `${v / 1000}k` : v)}
							/>

							{/* TOOLTIP */}
							<Tooltip
								cursor={{ fill: "rgba(255,255,255,0.04)" }}
								contentStyle={{
									background: "#14182f",
									border: "1px solid #2d3252",
									borderRadius: "6px",
									color: "#e5e7eb",
                }}
                labelFormatter={(label) => `Month: ${monthNames[label]}`}
							/>

							{/* GRADIENT */}
							<defs>
								<linearGradient id="barGradient" x1="0" y1="0" x2="0" y2="1">
									<stop offset="0%" stopColor="#4ade80" />
									<stop offset="100%" stopColor="#3b82f6" />
								</linearGradient>
							</defs>

							{/* BARS */}
							<Bar
								dataKey="income"
								fill="url(#barGradient)"
								radius={[6, 6, 0, 0]}
							/>
						</BarChart>
					</ResponsiveContainer>
				</>
			)}
		</div>
	);
};

export default MonthlyChart;

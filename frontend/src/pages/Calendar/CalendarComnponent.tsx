import { faArrowDown, faArrowUp } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

type CalendarDay = number | null;

type Props = {
	month: number;
	year: number;
};

const today = new Date();
const todaysDay = today.getDate();
const todaysMonth = today.getMonth();
const todaysYear = today.getFullYear();

const weekDays = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

function getCalendarDays(year: number, month: number): CalendarDay[] {
	const daysInMonth = new Date(year, month + 1, 0).getDate();
	const firstDay = new Date(year, month, 1).getDay();
	const startOffset = (firstDay + 6) % 7;
	const days: CalendarDay[] = [];

	for (let i = 0; i < startOffset; i++) {
		days.push(null);
	}

	for (let day = 1; day <= daysInMonth; day++) {
		days.push(day);
	}

	return days;
}

const CalendarComnponent = ({ month, year }: Props) => {
	const days = getCalendarDays(year, month);

	return (
		<div className="grid grid-cols-7 gap-1 text-center">
			{weekDays.map((d) => (
				<div key={d} className="text-gray-400 text-sm">
					{d}
				</div>
			))}

			{days.map((day, i) => (
				<div
					key={i}
					className={`p-2 rounded-md
				      ${
							day &&
							`${
								todaysDay == day && todaysMonth == month && todaysYear == year
									? "bg-gray-600"
									: "bg-gray-800"
							} hover:bg-gray-600 cursor-pointer`
						}`}>
					{!day ? (
						""
					) : (
						<>
							<div className="text-start md:ps-5 font-semibold text-2xl">{day}</div>
							<div className="border-b border-gray-700"></div>
							<div className="text-center text-green-800">
								<span className="w-8">
									<FontAwesomeIcon icon={faArrowUp} />
								</span>
								{day}
							</div>
							<div className="text-center text-red-800">
								<span className="w-8">
									<FontAwesomeIcon icon={faArrowDown} />
								</span>
								{day}
							</div>
						</>
					)}
				</div>
			))}
		</div>
	);
};

export default CalendarComnponent;

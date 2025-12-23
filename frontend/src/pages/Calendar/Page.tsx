import MenuBox from "../../shared/components/MenuBox";
import MonthPicker from "../../shared/widgets/MonthPicker/MonthPicker";
import useMonthPicker from "../../shared/widgets/MonthPicker/useMonthPicker";
import CalendarComnponent from "./CalendarComnponent";


const Calendar = () => {
	const monthPickerData = useMonthPicker();

	return (
		<>
			<MenuBox>
				<h1 className="text-3xl font-semibold ">Calendar</h1>
			</MenuBox>

			<MenuBox className="mt-3">
				<MonthPicker usePickerData={monthPickerData} />
				<CalendarComnponent month={monthPickerData.month} year={monthPickerData.year}/>
			</MenuBox>
		</>
	);
};

export default Calendar;

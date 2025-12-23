import { faAngleLeft, faAngleRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import type { Dispatch, SetStateAction } from "react";

type Props = {
	usePickerData: {
		month: number;
		year: number;
		label: string;
		nextMonth: () => void;
    prevMonth: () => void;
    setMonth: Dispatch<SetStateAction<number>>;
    setYear: Dispatch<SetStateAction<number>>;
	};
};

const MonthPicker = ({ usePickerData }: Props) => {
	return (
		<div className="flex items-center justify-center gap-2 mb-2">
			<button className="cursor-pointer backdrop-blur-sm border border-gray-700 rounded-xl shadow-lg p-2 hover:bg-gray-600" onClick={usePickerData.prevMonth}>
				<span className="w-8">
					<FontAwesomeIcon icon={faAngleLeft} size="lg" />
				</span>
      </button>
      
      <div className="text-2xl font-semibold shadow-lg p-2 w-60 text-center">{ usePickerData.label }</div>
      
			<button className="cursor-pointer backdrop-blur-sm border border-gray-700 rounded-xl shadow-lg p-2 hover:bg-gray-600" onClick={usePickerData.nextMonth}>
				<span className="w-8">
					<FontAwesomeIcon icon={faAngleRight} size="lg" />
				</span>
			</button>
		</div>
	);
};

export default MonthPicker;

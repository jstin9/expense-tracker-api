import { useCallback, useMemo, useState } from "react";

const today = new Date();

const useMonthPicker = () => {
	const [month, setMonth] = useState(today.getMonth());
	const [year, setYear] = useState(today.getFullYear());

	const label = useMemo(() => {
		const date = new Date(year, month, 1);

		return new Intl.DateTimeFormat("en-US", {
			month: "long",
			year: "numeric",
		}).format(date);
	}, [month, year]);

	const nextMonth = useCallback(() => {
		setMonth((prevMonth) => {
			if (prevMonth === 11) {
				setYear((y) => y + 1);
				return 0;
			}
			return prevMonth + 1;
		});
	}, []);

	const prevMonth = useCallback(() => {
		setMonth((prevMonth) => {
			if (prevMonth === 0) {
				setYear((y) => y - 1);
				return 11;
			}
			return prevMonth - 1;
		});
	}, []);

	return {
		month,
		year,
    label,
    nextMonth,
    prevMonth,
		setMonth,
		setYear,
	};
};

export default useMonthPicker;

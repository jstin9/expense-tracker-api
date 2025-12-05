import { useMemo, useState } from "react";

const useFilters = () => {
  const [type, setType] = useState<string>("");
  const [categoryId, setCategoryId] = useState<string>("");
  const [minAmount, setMinAmount] = useState<string>("");
  const [maxAmount, setMaxAmount] = useState<string>("");
  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");
  const [sort, setSort] = useState<string>("DESC");
  const [size, setSize] = useState<string>("20");

  const resetFilters = () => {
    setType("");
    setCategoryId("");
    setMinAmount("");
    setMaxAmount("");
    setStartDate("");
    setEndDate("");
    setSort("DESC");
    setSize("20");
  };

  const queryParams = useMemo(() => ({
    type,
    categoryId,
    minAmount,
    maxAmount,
    startDate,
    endDate,
    size,
    sort,
  }), [type, categoryId, minAmount, maxAmount, startDate, endDate, size, sort]);

  return {
    type, setType,
    categoryId, setCategoryId,
    minAmount, setMinAmount,
    maxAmount, setMaxAmount,
    startDate, setStartDate,
    endDate, setEndDate,
    sort, setSort,
    size, setSize,
    resetFilters,
    queryParams
  };
}

export default useFilters
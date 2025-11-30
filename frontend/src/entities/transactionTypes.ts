export interface Transaction {
  id: number;
  amount: number;
  type: "INCOME" | "EXPENSE";
  description: string;
  date: string;
  crudType: "POST" | "PUT" | "DELETE";
  category_id: string;
  category: { id: number, name: string }
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface Pageable {
  offset: number;
  paged: boolean;
  pageSize: number;
  pageNumber: number;
  sort: Sort;
  unpaged: boolean;
}

export interface TransactionPageResponse<T> {
  content: T[];

  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  numberOfElements: number;

  first: boolean;
  last: boolean;
  empty: boolean;

  sort: Sort;
  pageable: Pageable;
}

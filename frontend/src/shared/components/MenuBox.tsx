import type { ReactNode } from "react";

type Props = {
  children: ReactNode,
  className?: string,
  onClick?: (e: React.MouseEvent<HTMLDivElement, MouseEvent>) => void,
}

const MenuBox = ({ children, className, onClick }: Props) => {
	return (
    <div className={`bg-neutral-900/80 backdrop-blur-sm border border-neutral-800 rounded-2xl shadow-lg p-6 ${className}`} onClick={onClick}>
      { children }
		</div>
	);
};

export default MenuBox;

import { faX } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { createPortal } from "react-dom";
import MenuBox from "./MenuBox";

type Props = {
	title?: string;
	isOpen: boolean;
	showCloseButton?: boolean;
	onClose: () => void;
	children: React.ReactNode;
};

const Modal = ({ title, isOpen, showCloseButton = true, onClose, children }: Props) => {
	if (!isOpen) return null;

	return createPortal(
		<>
			<div className="fixed inset-0 bg-black opacity-50 flex items-center justify-center z-49" onClick={onClose}></div>
			<div className="fixed inset-0 flex items-center justify-center z-50">
				<MenuBox className="w-3/4 md:w-1/3 lg:w-1/4 relative flex flex-col">
					<div className="flex min-h-8 border-b border-gray-700 mb-4">
						<h1 className="text-2xl">{title}</h1>

						{showCloseButton && (
							<button
								onClick={onClose}
								className="absolute top-6 right-6 text-gray-500 hover:text-gray-200 transition">
								<FontAwesomeIcon icon={faX} size="xl" />
							</button>
						)}

					</div>

					{children}
				</MenuBox>
			</div>
		</>,
		document.body,
	);
};

export default Modal;

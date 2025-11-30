import { useState } from "react";
import Modal from "../components/Modal";
import ProfileForm from "../widgets/ProfileForm";


const useProfileFormWidget = () => {
	const title = "Complete Your Profile";
	const [modalIsOpen, setModalIsOpen] = useState(false);

	const handleOnCloseModal = () => {
		setModalIsOpen(false);
	};

	return {
		setModalIsOpen,
		modal: (
			<Modal title={title} isOpen={modalIsOpen} showCloseButton={false} onClose={handleOnCloseModal}>
				<ProfileForm onSubmit={handleOnCloseModal} />
			</Modal>
		),
	};
};

export default useProfileFormWidget;

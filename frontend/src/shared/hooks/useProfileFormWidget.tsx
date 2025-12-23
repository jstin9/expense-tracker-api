import { useState } from "react";
import Modal from "../components/Modal";
import ProfileForm from "../widgets/ProfileForm";

// i know it's not a correct way to create a hook but idk how else to do it :(
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

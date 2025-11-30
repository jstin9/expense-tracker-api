const Home = () => {
	const key = localStorage.getItem("token");
	return (
		<>
			<div className="w-full max-w-md bg-neutral-900/80 backdrop-blur-sm border border-neutral-800 rounded-2xl shadow-lg p-8">
				Home - {key}
			</div>
		</>
	);
};

export default Home;

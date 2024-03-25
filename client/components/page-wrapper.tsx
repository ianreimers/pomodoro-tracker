
interface Props {
	children: React.ReactNode
}

export function PageWrapper({ children }: Props) {
	return (
		<div className="min-w-screen min-h-[90vh] flex items-start justify-center">
			<div className="w-full md:max-w-6xl">{children}</div>
		</div>
	);
}

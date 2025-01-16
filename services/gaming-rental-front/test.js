async function main() {
	await Promise.all(
		Array.from({ length: 1000 }).map(
			async (v, idx) =>
				new Promise((resolve) =>
					setTimeout(() =>
						resolve(
							fetch(
								'http://192.168.0.175:3000/devices?sort=desc',
								{
									method: 'GET',
									headers: {
										'Content-Type': 'application/json',
									},
								}
							).then((res) => console.log(res.status, idx + 1))
						), 10 * idx
					)
				)
		)
	);
}

main();
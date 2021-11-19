package com.sirdave.portfolio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

class PortfolioApplicationTests {
	Calculator underTest = new Calculator();

	@Test
	void itShouldAdd() {
		int n1 = 20;
		int n2 = 30;

		int result = underTest.add(n1, n2);

		assertThat(result).isEqualTo(50);
	}

	static class Calculator{
		int add(int a, int b){
			return a + b;
		}
	}

}

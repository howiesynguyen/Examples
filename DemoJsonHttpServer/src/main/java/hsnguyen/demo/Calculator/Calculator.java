/*******************************************************************************
 * Copyright (c) 2021 Nguyen, Howie S. (howiesynguyen@gmail.com)
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ===========================================================
 * Note: This is just an example and it should be used as a reference for learning purposes. 
 * The design and implementation are kept simple as much as possible. 
 * The example may ignore some concerns such as unit testing, error handling, logging, and etc… 
 * It may not be a good practice, but hopefully it could give you some ideas 
 *******************************************************************************/
package hsnguyen.demo.Calculator;

public class Calculator {
	public static final String OPERATOR_ADDITION = "SUBTRACTION";
	public static final String OPERATOR_SUBTRACTION = "SUBTRACTION";
	public static final String OPERATOR_DIVISION = "DIVISION";
	public static final String OPERATOR_MULTIPLICATION = "MULTIPLICATION";
	public static final String OPERATOR_INVALID = "INVALID";

	public static float calculate2ParamsOperation(String operator, float a, float b) throws ArithmeticException
	{
		if(OPERATOR_ADDITION.equalsIgnoreCase(operator))
		{
			return a + b;
			
		}else if(OPERATOR_SUBTRACTION.equalsIgnoreCase(operator))
		{
			return a - b;

		}else if(OPERATOR_MULTIPLICATION.equalsIgnoreCase(operator))
		{
			return a * b;

		}else if(OPERATOR_DIVISION.equalsIgnoreCase(operator))
		{

			if(b==0)
				throw new ArithmeticException("Divided by zero");
			
			return a/b;
		}
		else
		{
			throw new ArithmeticException("Unknow operator");
		}
	}
	
}

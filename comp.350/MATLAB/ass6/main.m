%test1 = RecursionTrapezoid(0,1);
%sprintf("Displaying test1: %f", test1)

fprintf("ERF(1) = %0.8f\n", erf(1));

[test1, x] = RecursionTrapezoid(0,1);
fprintf("Recursion Trapezoid: %8f \nNumber of iterations: %.1f \n", test1, x)

error1 = erf(1) - test1;
fprintf("Error: %0.8f\n", error1);

test2 = adapt_simpson( 0, 1, 10^(-6), 0, 100, 0);
fprintf("Adapted Simpson method: %8f \nNumber of iterations: %.1f\n", test2);

error2= erf(1) - test2(1);
fprintf("Error: %0.11f\n", error2);



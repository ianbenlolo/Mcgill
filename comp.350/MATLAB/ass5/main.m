function plt=main()


hold on
fp=fplot(@newtonEval,[-1,1]);
fp.Color=[1,1,0];
fp.DisplayName='Newton (p(x))';

% printing Spline
fp2=fplot(@cubicSpline,[-1,1]);
fp2.Color=[0,1,0];
fp2.DisplayName='Spline (S(x))';

%least squares
fp3=fplot(@leastSquaresEval,[-1,1]);
fp3.Color=[0,0,1];
fp3.DisplayName='Least Squares fit g(x)';

fp4=fplot(@f,[-1,1]);
fp4.Color=[1,0,1];
fp4.DisplayName='f(x)';

legend
title('Function interpolation using 7 equally spaced nodes on [-1,1]')
%title('Function interpolation using 7 Chebyshev spaced nodes on [-1,1]')
xlabel('x')
ylabel('y')
hold off

disp('------------')


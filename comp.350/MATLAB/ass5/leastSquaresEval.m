function g=leastSquaresEval(x)
coord=0;
p=leastSquares(coord);
g=p(1)+p(2)*x.^2+p(3)*x.^4;
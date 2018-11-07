function genpOpt(A,D,b)
n = length(b);
for k = 1:(n+1)/2 - 1
    mult = A(k)/D(k);
    q = n-k+1;
    A(k) = 0;
    D(q) = D(q) - mult*A(q);
    b(q) = b(q) - mult*b(k);
end

x = zeros(n,1);
x(n) = b(n)/D(n);
for k = n-1:-1:(n+1)/2
    x(k) = b(k)/D(k);
end
for k = ((n+1)/2 - 1):-1:1
    x(k) = (b(k)-A(n-k+1)*x(n-k+1))/D(k);
end
    
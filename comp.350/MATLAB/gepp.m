%ian
function x = gepp(A,b)
%input: A is an n x n nonsingular matrix
%       b is an n x 1 vector
% output: x is the solution of Ax=b
%A is nxn so n^2 memory; b is nx1 for n memory
n = length(b);

midpoint = (n+1)/2; % n is odd so this is a whole number
for k = 1:n-1
    
    [maxval, maxindex] = max(abs(A(k:n,k)));
    
    if maxval==0, error('A is singular'), end
    
    q = maxindex+k-1;
    A([k,q],k:n) = A([q,k],k:n);
   
    b([k,q]) = b([q,k]);
    if k == midpoint
        continue;
    else
        i = k+1:n;
        A(i,k) = A(i,k)/A(k,k);
        A(i,i) = A(i,i) - A(i,k)*A(k,i);
        b(i) = b(i) - A(i,k)*b(k);
    end
end
x = zeros(n,1); %%backwards substitution
x(n) = b(n)/A(n,n);
for k = n-1:-1:1
    x(k) = (b(k) - A(k,k+1:n)*x(k+1:n))/A(k,k);
end

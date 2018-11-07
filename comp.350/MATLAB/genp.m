%ian
function x =genp(A,b)
% input (nonsingular) n by n matrix A and n by 1 matrix b
%output solution to Ax=b
%A is nxn so n^2 memory; b is nx1 for n memory

n=length(b);
midpoint = (n+1)/2 ; %2 flops, 1 memory

for k=1:n-1
    if k==midpoint
        continue;
    else
        i=k+1:n;
        A(i,k) =A(i,k)/A(k,k);
        A(i,i)=A(i,i)-A(i,k)*A(k,i);
        b(i)=b(i)-A(i,k)*b(k);
    end
end

%back substitution
x=zeros(n,1);
x(n)=b(n)/A(n,n);
for k=n-1:-1:1
    x(k)=(b(k)-A(k,k+1:n)*x(k+1:n))/A(k,k);
end

end

function geppOpt(d1,d2,b)
%algo gauss elimination with partial pivoting
% input the two diagonals d1 and d2 and b  s.t. Ax=B
%and the diagonals of A are d1 and d2
% these are all stored in arrays to save space

n=length(b);

for k=1:(n+1)/2-1 %only untill (n-1)/2 because 
    if d1(k)<d2(k) %well swap them
        temp=d2(k);
        d2(k)=d1(k);
        d1(k)=temp;
    end
    mult=d1(k)/d2(k);
    i=n-k+1;
    d1(k)=0;
    d2(i)=d(i)-mult*d1(i);
    b(i)=b(i)-mult*b(k);
end

%back substitution
x=zeros(n,1);
x(n)=b(n)/d2(n);
for k=n-1:-1:(n+1)/2
    x(k)=(b(k)-d1(n-k+1)*x(n-k+1))/d2(k);
end
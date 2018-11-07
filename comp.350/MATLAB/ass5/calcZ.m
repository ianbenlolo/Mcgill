function z=calcZ(coords)

if coords==0
    x=linspace(-1,1,7);
elseif coords==1 %for chebyshev
    for i=0:6
        x(i+1)=cos((2*i+1)*pi/(14));
    end
    x=fliplr(x);
end

n=length(x);

for i=1:n
    y(i)=f(x(i));
end


for i=1:n-1
    h(i)=x(i+1)-x(i);
    b(i)=(y(i+1)-y(i))/h(i);
end


%forward elimination
u(2)=2*(h(1) + h(2));
v(2)=6*(b(2) - b(1));

for i=3:n-1
    u(i)=2*(h(i-1)+h(i)) - ((h(i-1))^2)/u(i-1);
    v(i)=6*(b(i)-b(i-1))-(h(i-1)*v(i-1))/u(i-1);
end

%back substitution
z(n)=0;
for i=n-1:-1:2
    z(i)=(v(i)-h(i)*z(i+1))/u(i);
end
z(1)=0;

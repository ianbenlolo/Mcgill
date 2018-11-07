function S=cubicSpline(x)
coord=0;
if coord==0
    t=linspace(-1,1,7);
elseif coord==1 %for chebyshev
    for i=0:6
        t(i+1)=cos((2*i+1)*pi/(14));
    end
    t=fliplr(t);
end
n=length(t);
z=calcZ(coord);
for i=1:n
    y(i)=f(t(i));
end

for i=1:n-1
    if x<=t(i+1)
        break;
    end
end
h = t(i+1)-t(i);
B = -(h*z(i+1)/6)-(h*(z(i))/3) + ( y(i+1) - y(i))/h;
D = ( z(i+1)-z(i))/(6*h);
S = y(i) + (x-t(i))*(B + (x-t(i))*( (z(i)/2) + ((x - t(i))*D)));
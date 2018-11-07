function testscript()

n=9;
diagonal1 = randn(1, 2*n+1);
diagonal2 = randn(1, 2*n+1);

matrix1 = zeros(2*n+1, 2*n+1);
matrix1=diag(diagonal1);
A=fliplr(matrix1);
A=A+diag(diagonal2); % Random A is done
b=zeros(2*n+1,1);

midpt=n+1;
%since x = one's, we wnat to add all the horizontal entries in Ai into bi

for i=1:2*n+1
    if i==midpt
        b(i,1)=A(i,i);
    else
        b(i,1)=A(i,i)+A(i,2*n+2-i);
    end
end
% at this point we have A and b such that the solution to Ax=b is a matrix of ones

%%now well test my genp and gepp programs

Xnp=genp(A,b); 
Xpp=gepp(A,b); 
x=ones(2*n+1,1);
epsilon=eps*cond(A,2); % this calculates epsilon*norm(A)*norm(A^-1)

disp("-----------------------------------------------------------------")
XnpErr = (norm((x-Xnp),2))/(norm(x,2)); %errors for GEPP and GENP
XppErr = (norm((x-Xpp),2))/(norm(x,2)); % these should be 0 but aren't due to discretization error of the computer!

disp("Data: ")
disp(" A= ")
disp(A)
disp("b = ")
disp(b)

disp("Result for ")
disp("     GENP      GEPP")
disp([Xnp, Xpp])

disp("Error in Xnp= "+ XnpErr)
disp("Error in Xpp= "+ XppErr)
disp("Epsilon*||A||*||A^1||= "+epsilon)

disp("")
disp("*****************************************************************") %just some spacing
disp("")

disp("Question 2.c)")

A(1,1)=10e-15;
b(1,1)=A(1,1)+A(1,2*n+2-1); %% made the changes required by the question

disp("Data: ")
disp(" A= ")
disp(A)
disp("b = ")
disp(b)

Xnp=genp(A,b);  %%now we calculate and print everything as before
Xpp=gepp(A,b); 
epsilon=eps*cond(A,2); 
XnpErr = (norm((x-Xnp),2))/(norm(x,2)); %errors for GEPP and GENP
XppErr = (norm((x-Xpp),2))/(norm(x,2)); % these should be 0 but aren't due to discretization error of the computer!


disp("Result for ")
disp("     GENP      GEPP")
disp([Xnp, Xpp])

disp("Xnp error: "+ XnpErr)
disp("Xpp error: "+ XppErr)
disp("Epsilon*||A||*||A^1||= "+epsilon)

disp("*****************************************************************") %just some spacing

disp("Question 2.d)")
A(2*n+1,1)=10e-8;
b(2*n+1,1)=A(2*n+1,1)+A(2*n+1,2*n+2-1); %% made the changes required by the question
%now we calculate and print everything as before

disp("Data: ")
disp(" A= ")
disp(A)
disp("b = ")
disp(b)

Xnp=genp(A,b);  
Xpp=gepp(A,b); 
epsilon=eps*cond(A,2); 

XnpErr = (norm((x-Xnp),2))/(norm(x,2)); %errors for GEPP and GENP
XppErr = (norm((x-Xpp),2))/(norm(x,2)); % these should be 0 but aren't due to discretization error of the computer!

disp("Result for ")
disp("     GENP      GEPP")
disp([Xnp, Xpp])
disp("Xnp error: "+ XnpErr)
disp("Xpp error: "+ XppErr)
disp("Epsilon*||A||*||A^1||= "+epsilon)


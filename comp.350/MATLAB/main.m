disp("-------------------------------------------------")
disp("Newton with x0=3.0")
Nroot1 = newton('f', 'fd', 3.0, 1e-14, 0, 40, 1)
disp("")
disp("Newton with x0=2.5")
Nroot2 = newton('f', 'fd', 2.5, 1e-14, 0, 40, 1)

disp("-------------------------------------------------")
disp("Secant with x0=3, x1=2.5")
SecantRoot1 = secant('f', 2.5, 3, 1e-14, 0, 40, 1)
disp("-------------------------------------------------")
disp("Steffensen with x0=3")
StefRoot1 = steffensen('f', 3.0, 1e-14, 0, 40,1)
disp("")
disp("Steffensen with x0=2.5")
StefRoot2 = steffensen('f', 2.5, 1e-14, 0, 40,1)



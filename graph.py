import pandas as pd
import matplotlib.pyplot as plt
import glob
import numpy as np

vents = [0, 2, 4, 6, 8, 10]
nRuns = 10

means = []
stds = []

for v in vents:
    files = glob.glob(f"summary_wind{v}_run*.csv")
    finals = []
    for f in files:
        df = pd.read_csv(f)
        finals.append(df['percent_burned'].iloc[-1])
    if finals:
        means.append(np.mean(finals))
        stds.append(np.std(finals))
    else:
        means.append(float('nan'))
        stds.append(float('nan'))

plt.figure(figsize=(10,6))
plt.errorbar(vents, means, yerr=stds, fmt='-o', capsize=5)
plt.xlabel("Vitesse du vent (m/s)")
plt.ylabel("Pourcentage forêt brûlée (final)")
plt.title("Diagramme de phase : forêt brûlée VS vent")
plt.grid(True)
plt.tight_layout()
plt.show()

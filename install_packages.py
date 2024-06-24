import subprocess
import sys

def install(package):
    subprocess.check_call([sys.executable, "-m", "pip", "install", package])

# List of packages to install
packages = [
    "flask",
    "scikit-learn",
    "sqlalchemy",
    "pandas"
    "mysql-connector-python"
]

# Install each package
for package in packages:
    install(package)

print("All packages installed successfully.")

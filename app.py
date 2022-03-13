from flask import Flask
import os, subprocess

app = Flask(__name__)


@app.route('/', methods=['GET'])
def run():
    a_file=open('containers.txt','r')
    lines = a_file.readlines()
    for line in lines:
        return line


if __name__ == "__main__":
    port = int(os.environ.get('PORT', 5000))
    app.run(debug=True, host='0.0.0.0', port=port)

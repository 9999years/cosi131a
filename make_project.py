import argparse
import os
import re
import shutil
from dataclasses import dataclass
import functools
from pathlib import Path

from termcolor import colored

@dataclass(order=True)
class PAVersion:
    major: int = 1
    minor: str = None

    def increment_minor(self):
        if self.minor:
            if ord(self.minor) > ord('Z'):
                raise ValueError
            self.minor = chr(ord(self.minor) + 1)
        else:
            self.minor = 'A'

    def increment_major(self):
        self.major += 1
        self.minor = None

    @property
    def minor_str(self):
        return self.minor or ''

    @property
    def major_str(self):
        return str(self.major)

    def __str__(self):
        return f'{self.major}{self.minor_str}'


def nospaces(x):
    if ' ' in x:
        raise ValueError
    return x

def next_pa(dirnames=(), is_minor=False):
    pa_num = re.compile(r'(\d+)([A-Z]?)')
    pas = []
    for pa in dirnames:
        match = pa_num.match(pa)
        if match:
            major, minor = match.groups()
            major = int(major)
            pas.append(PAVersion(major, minor))
    if not pas:
        return '1'
    pas.sort()
    ver = pas[-1]
    if is_minor:
        ver.increment_minor()
    else:
        print('semifinal:', ver)
        ver.increment_major()
        print('final:', ver)
    return str(ver)

def test_next_pa():
    """for pytest"""
    assert '1' == next_pa()
    assert '1' == next_pa([])
    assert '1' == next_pa(['0'])
    assert '2' == next_pa(['1'])
    assert '1A' == next_pa(['1'], is_minor=True)
    assert '1B' == next_pa(['1A'], is_minor=True)
    assert '2' == next_pa(['1', '1A', '1C'])
    assert '3' == next_pa(['sadogjasoig', '2', '1', '1A', '1C'])

idea = functools.partial(os.path.join, '.idea')

def make_project(name, number,
        group_id='ooo.becca.cosi131a', artifact_id_fmt='rebeccaturner-PA{number}-{name}',
        whatif=False, force=False):
    dirname = f'{number}-{name}'
    artifact_id = artifact_id_fmt.format(number=number, name=name)

    def print_row(name, value):
        print(name, colored(value, 'green'))

    print_row('directory  ', dirname)
    print_row('groupId    ', group_id)
    print_row('artifactId ', artifact_id)
    print_row('path       ', os.path.abspath(dirname))

    if whatif:
        return

    if os.path.exists(dirname) and not force:
        raise ValueError(dirname + ' already exists')

    shutil.copytree('boilerplate', dirname, copy_function=shutil.copy)
    os.chdir(dirname)
    os.makedirs('src/main/java', exist_ok=True)
    os.makedirs('src/test/java', exist_ok=True)

    def replace(fname):
        """replace {ARTIFACT_ID}, etc. w/ proper values"""
        nonlocal group_id, artifact_id

        with open(fname, 'r') as fh:
            dat = fh.read()
        dat = dat.replace('{GROUP_ID}', group_id)
        dat = dat.replace('{ARTIFACT_ID}', artifact_id)
        with open(fname, 'w') as fh:
            fh.write(dat)

    # mutating pom.xml
    replace('pom.xml')
    replace('README.md')
    replace(idea('compiler.xml'))
    replace(idea('modules.xml'))
    replace(idea('workspace.xml'))

    with open(idea('.name'), 'w') as namefile:
        namefile.write(artifact_id)

    shutil.move(idea('template.iml'), idea(artifact_id + '.iml'))

def main():
    parser = argparse.ArgumentParser(description='''Creates project boilerplate
            by copying stub files in the boilerplate/ directory; also
            increments the PA number and sets the proper groupId and artifactId
            in the pom.xml''')

    parser.add_argument('-m', '--minor', action='store_true',
            help='''Indicates that this is a "minor" assignment and only
            increments the number by one letter''')
    parser.add_argument('-n', '--number', type=nospaces,
            help='''PA number; 1, 2A, 12C, etc.''')
    parser.add_argument('-i', '--id-format', default='''rebeccaturner-PA{number}-{name}''',
            help='''Python format-string; valid variables are `name` (project
            name) and `number` (project number); defaults to `rebeccaturner-PA{number}-{name}`''')
    parser.add_argument('-w', '--whatif', action='store_true',
            help='''Don't actually create a project''')
    parser.add_argument('-g', '--group-id', type=nospaces, default='edu.brandeis.cosi131a',
            help='''groupId used in pom.xml; defaults to edu.brandeis.cosi131a''')
    parser.add_argument('-f', '--force', action='store_true',
            help='If given, overwrite files/folders even if they exist')

    parser.add_argument('project_name', type=nospaces,
            help='''Display name; used in the artifact ID''')

    args = parser.parse_args()

    num = args.number or next_pa(
            filter(os.path.isdir, os.listdir()),
            is_minor=args.minor)

    make_project(args.project_name, num,
            group_id=args.group_id, artifact_id_fmt=args.id_format,
            whatif=args.whatif, force=args.force)

if __name__ == '__main__':
    try:
        main()
    except Exception as e:
        print(colored(str(e), 'red'))

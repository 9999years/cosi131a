import argparse
import os
import re
import shutil

from termcolor import colored

def nospaces(x):
    if ' ' in x:
        raise ValueError
    return x

def next_pa(dirnames=None, is_minor=False):
    pa_num = re.compile(r'^(\d+)\.?(\d+)?')
    pas = []
    for pa in dirnames:
        match = pa_num.match(pa)
        if match:
            pas.append((*map(
                lambda pa: None if pa is None else int(pa),
                match.groups()),))
    if not pas:
        return '1'
    pas.sort()
    major, minor = pas[-1]
    if is_minor:
        if minor is None:
            minor = 1
        minor += 1
    else:
        major += 1
    return str(major) + ('.' + str(minor) if is_minor else '')

def make_project(name, number,
        group_id='ooo.becca.cosi131a', artifact_id_fmt='rebeccaturner-PA{number}-{name}',
        whatif=False):
    dirname = f'{number}-{name}'
    artifact_id = artifact_id_fmt.format(number=number, name=name)

    def print_row(name, value):
        print(name, colored(value, 'green'))

    print_row('directory  ', dirname)
    print_row('groupId    ', group_id)
    print_row('artifactId ', artifact_id)

    if whatif:
        return

    if os.path.exists(dirname):
        raise ValueError(dirname + ' already exists')

    shutil.copytree('boilerplate', dirname, copy_function=shutil.copy)
    os.chdir(dirname)

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
    replace(os.path.join('.idea', 'compiler.xml'))
    replace(os.path.join('.idea', 'modules.xml'))
    replace(os.path.join('.idea', 'workspace.xml'))

    with open(os.path.join('.idea', '.name'), 'w') as namefile:
        namefile.write(artifact_id)

    shutil.move('template.iml', artifact_id + '.iml')

def main():
    parser = argparse.ArgumentParser(description='''Creates project boilerplate
            by copying stub files in the boilerplate/ directory; also
            increments the PA number and sets the proper groupId and artifactId
            in the pom.xml''')

    parser.add_argument('-m', '--minor', action='store_true',
            help='''Indicates that this is a "minor" assignment and only
            increments the number by 0.1''')
    parser.add_argument('-n', '--number', type=nospaces,
            help='''PA number; 1.1, 2.4, etc.''')
    parser.add_argument('-i', '--id-format', default='''rebeccaturner-PA{number}-{name}''',
            help='''Python format-string; valid variables are `name` (project
            name) and `number` (project number); defaults to `rebeccaturner-PA{number}-{name}`''')
    parser.add_argument('-w', '--whatif', action='store_true',
            help='''Don't actually create a project''')
    parser.add_argument('-g', '--group-id', type=nospaces, default='ooo.becca.cosi131a',
            help='''groupId used in pom.xml; defaults to ooo.becca.cosi131a''')

    parser.add_argument('project_name', type=nospaces,
            help='''Display name; used in the artifact ID''')

    args = parser.parse_args()

    num = args.number or next_pa(
            filter(os.path.isdir, os.listdir()),
            is_minor=args.minor)

    make_project(args.project_name, num,
            group_id=args.group_id, artifact_id_fmt=args.id_format,
            whatif=args.whatif)

if __name__ == '__main__':
    try:
        main()
    except Exception as e:
        print(colored(str(e), 'red'))

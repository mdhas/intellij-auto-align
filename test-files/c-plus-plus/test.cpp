std::vector<std::string>		Parser::parseLine(std::string& line);
{
  std::vector<std::string>		ret;
  regmatch_t					match;
  int							no_match;
  int							start_match;
  int							end_match;
  while (!(no_match = regexec(&this->_regex, line.c_str(), 1, &match, 0)))
    {
       start_match = match.rm_so;
       end_match = match.rm_eo;
       ret.push_back(std::string(line, start_match, (end_match - start_match)));
       line.assign(line, end_match, -1);
    }

  if (no_match != REG_NOMATCH) {
    throw (exception("Regex match failed"));
  }
  return ret;
}

std::vector<std::string>		Parser::parse()
{
  std::vector<std::string> ret;
  std::vector<std::string> tmp;
  std::vector<std::string> line;

  while (std::getline(this->_file, line))
    {
      tmp = this->parseLine(line);
      ret.insert(ret.end(), tmp.begin(), tmp.end());
    }
  return (ret);
}
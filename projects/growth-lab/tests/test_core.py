import unittest,sys,json
from pathlib import Path
from unittest.mock import patch
sys.path.insert(0,str(Path(__file__).resolve().parents[1]))
from core import *
class Workflows(unittest.TestCase):
 def test_hot(self):
  r=qualify(SAMPLE_LEAD); self.assertEqual(r['score'],100); self.assertEqual(r['tier'],'Hot'); self.assertTrue(r['review_required'])
 def test_nurture(self):
  r=qualify(dict(SAMPLE_LEAD,industry='other',service='other',budget=0,timeline_days=200,decision_maker=False)); self.assertEqual(r['score'],0); self.assertEqual(r['tier'],'Nurture')
 def test_warm(self):
  r=qualify(dict(SAMPLE_LEAD,budget=0,timeline_days=200,decision_maker=False)); self.assertEqual(r['score'],45); self.assertEqual(r['tier'],'Warm')
 def test_invalid_budget(self):
  for v in [-1,'oops',float('nan'),float('inf'),True]:
   with self.assertRaises(ValueError): qualify(dict(SAMPLE_LEAD,budget=v))
 def test_boolean_required(self):
  with self.assertRaises(ValueError): qualify(dict(SAMPLE_LEAD,decision_maker='yes'))
 def test_missing_fields(self):
  with self.assertRaises(ValueError): brief({})
 def test_seo(self):
  r=brief(SAMPLE_SEO); self.assertEqual(len(r['outline']),5); self.assertLessEqual(len(r['meta_description']),160); self.assertNotIn(' ',r['slug']);self.assertEqual(len(r['keywords']),3)
 def test_unknown_mode(self):
  with self.assertRaises(ValueError): run('seo',SAMPLE_SEO,'fake')
 def test_unknown_tool(self):
  with self.assertRaises(ValueError): run('fake',{})
 def test_no_model_no_fake_success(self):
  with patch.dict(os.environ,{'OLLAMA_MODEL':''}):
   with self.assertRaises(ValueError): qualify(SAMPLE_LEAD,'ollama')
 def test_model_qualification_preserves_score(self):
  with patch('core.chat',return_value={'draft_reply':'Thanks for the enquiry.','follow_up_questions':['What is your goal?']}):
   r=qualify(SAMPLE_LEAD,'ollama');self.assertEqual(r['score'],100); self.assertEqual(r['draft_reply'],'Thanks for the enquiry.')
 def test_model_brief_validation(self):
  with patch('core.chat',return_value={'title':'Bad'}):
   with self.assertRaises(ValueError): brief(SAMPLE_SEO,'ollama')
if __name__=='__main__':unittest.main()
